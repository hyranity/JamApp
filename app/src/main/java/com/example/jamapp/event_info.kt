package com.example.jamapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.example.jamapp.Model.Event
import com.example.jamapp.Model.Report
import com.example.jamapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_announce.*
import kotlinx.android.synthetic.main.fragment_event.*
import com.example.jamapp.Util.sendEmail
import kotlinx.android.synthetic.main.fragment_edit_event.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class event_info : AppCompatActivity() {
    // Get event object from passed intent
    public lateinit var event : Event
    private lateinit var emailList: ArrayList<String>
    private lateinit var db : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        emailList = ArrayList<String>()

        // Get extra
        event =  intent.getSerializableExtra("event") as Event

        // Check if user already registered to this event or not
        val ref = db.child("users").child(auth.currentUser!!.uid).child("Participating")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError : DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                Log.d("Attendees","Checking for existence")

                // Check if User hasn't joined Event
                if (!snapshot.hasChild(event.event_id)) {
                    isRegistered = false;
                    register_button.setText("I'M IN!")

                    // Check if User has joined Event
                } else {
                    isRegistered = true;
                    register_button.setText("UNATTEND!")
                }
            }
        })

        // if this is the host
        if (event.host_id == auth.currentUser!!.uid) {
            // Get attendees' email
            db.child("event").child(event.event_id).child("Attendees")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Can't obtain attendees", databaseError.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (item in dataSnapshot.children) {
                            // For each ID
                            val userId = item.getValue(String::class.java) as String
                            // Get user object
                            db.child("users").child(userId)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e("Can't obtain attendees", databaseError.message)
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Add user's email
                                        val user = dataSnapshot.getValue(User::class.java) as User
                                        emailList.add(user.email)
                                    }
                                })
                        }
                    }

                })
        }


        setContentView(R.layout.activity_event_info)

    }

    // Go back to event fragment
    public fun backToEventInfo(view : View){
        view.findNavController().navigate(R.id.action_report_event_to_event)
    }

    // Go to report event screen
    public fun reportEvent(view : View){
        view.findNavController().navigate(R.id.action_event_to_report_event)
    }

    // Back to home
    public fun backToHome(view : View){
        finish()
    }

    // From attendance viewing back to event
    public fun attendanceToEvent(view: View) {
        view.findNavController().navigate(R.id.action_attendanceList_to_event)
    }

    // From event to attendance viewing
    public fun eventToAttendance(view: View) {
        view.findNavController().navigate(R.id.action_event_to_attendanceList)
    }


    // When I'm In button is pressed
    public fun imIn(view : View) {
       if(isRegistered){
           unregisterUser()
           isRegistered = false
           register_button.setText("I'M IN!")

           // Decrease count in db
           event.attendanceCount = event.attendanceCount-1
           db.child("event").child(event.event_id).child("attendanceCount").setValue(event.attendanceCount)

           // Show success message
           val toast = Toast.makeText(applicationContext, "You are no longer registered to this event,", Toast.LENGTH_SHORT)
           toast.show()
           }
        else {
           registerUser()
           isRegistered = true
           register_button.setText("UNATTEND!")

           // Increase count in db
           event.attendanceCount = event.attendanceCount+1
           db.child("event").child(event.event_id).child("attendanceCount").setValue(event.attendanceCount)

           val toast = Toast.makeText(applicationContext, "You are now registered to this event,", Toast.LENGTH_SHORT)
           toast.show()
       }

    }

    public fun registerUser(){
        val user = auth.currentUser
        // Add User ID to Attendees in Event object
        db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).setValue(user!!.uid) // https://stackoverflow.com/a/40013420

        //Add Event ID to Participations in User object
        db.child("users").child(user!!.uid).child("Participating").child(event.event_id).setValue(event.event_id)
    }

    public fun unregisterUser(){
        val user = auth.currentUser
        // Remove User ID from Attendees in Event object
        db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).removeValue()// removes the value

        //Remove Event ID from Participations in User object
        db.child("users").child(user!!.uid).child("Participating").child(event.event_id).removeValue()
    }

    // Submit a report about the event
    public fun submitReport(view : View){
        val reportTitle = findViewById(R.id.createReportTitle) as EditText
        val reportMsg = findViewById(R.id.createReportMsg) as EditText

        // VALIDATION : ensure all fields are filled in
        if (reportTitle.text.isEmpty() || reportMsg.text.isEmpty()) {
            val toast = Toast.makeText(applicationContext, "Ensure all fields are filled in.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit title input to 50 characters
        if (reportTitle.text.length > 50) {
            val toast = Toast.makeText(applicationContext, "Event title should not exceed 50 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit msg input to 200 characters
        if (reportMsg.text.length > 200) {
            val toast = Toast.makeText(applicationContext, "Event title should not exceed 200 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // Obtain report details
        val report = Report(
            reporter_id = auth.currentUser!!.uid,
            title = reportTitle.text.toString(),
            message = reportMsg.text.toString()
        )

        // Push to database
        db.child("event").child(event.event_id).child("reports").push().setValue(report)


        // Redirect
        backToEventInfo(view)
    }

    public fun goToViewReports(view: View) {
        view.findNavController().navigate(R.id.action_event_to_viewReports)
    }

    public fun reportsToEvent(view: View) {
        view.findNavController().navigate(R.id.action_viewReports_to_event)
    }

    public fun redirectEditEvent(view : View){
        view.findNavController().navigate(R.id.action_event_to_edit_event)
    }

    public fun eventBackToEventInfo(view: View) {
        view.findNavController().navigate(R.id.action_edit_event_to_event)
    }

    public fun eventToAnnounce(view: View) {
        view.findNavController().navigate(R.id.action_event_to_announcement)
    }

    public fun announceToEvent(view: View) {
        view.findNavController().navigate(R.id.action_announcement_to_event)
    }

    public fun eventToHelp(view: View) {
        view.findNavController().navigate(R.id.action_event_to_event_help)
    }

    public fun helpToEvent(view: View) {
        view.findNavController().navigate(R.id.action_event_help_to_event)
    }

    // For editing events
    public fun editEvent(view : View){
        // VALIDATION : ensure all fields are filled in
        if (editEventTitle.text.isEmpty() ||
                editEventDate.text.isEmpty() ||
                editEventVenue.text.isEmpty() ||
                editEventDescription.text.isEmpty() ||
                editEventImageUrl.text.isEmpty() ||
                editEventWebLink.text.isEmpty()) {
            val toast = Toast.makeText(applicationContext, "Ensure all fields are filled in", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit title input to 50 characters
        if (editEventTitle.text.length > 50) {
            val toast = Toast.makeText(applicationContext, "Event title should not exceed 50 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit title input to 80 characters
        if (editEventVenue.text.length > 80) {
            val toast = Toast.makeText(applicationContext, "Event venue should not exceed 80 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit title input to 200 characters
        if (editEventDescription.text.length > 50) {
            val toast = Toast.makeText(applicationContext, "Event description should not exceed 200 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION: ensure date input is valid
        try {
            val cal = Calendar.getInstance() as Calendar
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            cal.time = sdf.parse(editEventDate.text.toString())
        } catch (ex : ParseException) {
            val toast = Toast.makeText(applicationContext, "Date should use the format dd/MM/yyyy.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // Convert the string to calendar
        val cal = Calendar.getInstance() as Calendar
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(editEventDate.text.toString())

        // VALIDATION : ensure input date is after current date
        val currentTime = Calendar.getInstance() as Calendar
        if (!currentTime.before(cal)) {
            val toast = Toast.makeText(applicationContext, "Date of event should not be before the current date.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // Create new event object with updated details
        val updatedEvent = Event(
            event_id = event.event_id,
            host_id = event.host_id,
            address = findViewById<EditText>(R.id.editEventVenue).text.toString(),
            title = findViewById<EditText>(R.id.editEventTitle).text.toString(),
            description = findViewById<EditText>(R.id.editEventDescription).text.toString(),
            imageLink = findViewById<EditText>(R.id.editEventImageUrl).text.toString(),
            learnMoreLink = findViewById<EditText>(R.id.editEventWebLink).text.toString(),
            date = findViewById<EditText>(R.id.editEventDate).text.toString(),
            attendanceCount = event.attendanceCount
        )

        // Update in database
        db.child("event").child(event.event_id).setValue(updatedEvent)

        // Redirect
        view.findNavController().navigate(R.id.action_edit_event_to_event)

    }

    public fun deleteEvent(view: View) {
        var isClosed = false

        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Delete \"" + event.title + "\"?")
        dialogBuilder.setMessage("You are going to delete this event entirely. This action is not reversible. Are you sure?")

        // Send email to affected users
        var emailList = ArrayList<String>()

        // Set YES button
        dialogBuilder.setPositiveButton("YES") { dialog, which ->

            // Delete the event

            db.child("event").child(event.event_id).child("Attendees")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Delete Attendees", databaseError.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (attendee in dataSnapshot.children) {
                            // Delete each attendee's participation
                            // Remove the event from there
                            db.child("users").child(attendee.getValue(String::class.java) as String)
                                .child("Participating").child(event.event_id)
                                .removeValue()

                            // Get email of each user
                            db.child("users").child(attendee.getValue(String::class.java) as String)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e("Delete Attendees", databaseError.message)
                                    }

                                    override fun onDataChange(snapShot: DataSnapshot) {
                                        val user = snapShot.getValue(User::class.java) as User
                                        // Notify the user via email
                                        //com.example.jamapp.Util.sendSingleEmail(this@event_info, user.email, event.title + " has been deleted.", "This event has been deleted by its owner. You have been automatically unregistered from it.", event.title +" deleted")
                                    }
                                })
                        }
                    }
                })

            dialog.dismiss()

            // Delete the event itself
            db.child("event").child(event.event_id).removeValue()

            Toast.makeText(applicationContext, "Successfully deleted event", Toast.LENGTH_SHORT)
                .show()


        }

        dialogBuilder.setNegativeButton("NO") { dialog, which ->
            // Cancel
            isClosed = true
            Toast.makeText(applicationContext, "Event not deleted", Toast.LENGTH_SHORT).show()
        }

        // Show the dialog itself
        var dialog = dialogBuilder.show()

        // Close the dialog
        if (isClosed)
            dialog.cancel()
    }

    public fun sendEmail(view: View) {
        val title = announce_title.text.toString()
        val message = announce_desc.text.toString()

        // VALIDATION : ensure all fields are filled in
        if (announce_title.text.isEmpty() || announce_desc.text.isEmpty()) {
            val toast = Toast.makeText(applicationContext, "Ensure all fields are filled in.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit title input to 50 characters
        if (announce_title.text.length > 50) {
            val toast = Toast.makeText(applicationContext, "Event title should not exceed 50 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : limit desc input to 200 characters
        if (announce_desc.text.length > 200) {
            val toast = Toast.makeText(applicationContext, "Event title should not exceed 200 characters.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        val email = BackgroundMail.newBuilder(this)
            .withUsername("th3jamapp@gmail.com")
            .withPassword("jamapp123")
            .withType(BackgroundMail.TYPE_HTML)
            .withBody("<h1>" + title + "</h1><br/>" + message)
            .withSubject("Announcement for " + event.title)
            .withOnSuccessCallback {
                Toast.makeText(this, "Announcement successfully made", Toast.LENGTH_SHORT)
            }
            .withOnFailCallback {
                Toast.makeText(this, "Announcement  failed", Toast.LENGTH_SHORT)
            }


        // Send to all receipients
        for (receipientEmail in emailList) {
            email.withMailto(receipientEmail)
            Log.d("email", receipientEmail)
        }

        // Send email
        email.send()
    }

}
