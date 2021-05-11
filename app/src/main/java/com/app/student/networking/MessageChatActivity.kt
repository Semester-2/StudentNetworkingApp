package com.app.student.networking

import android.app.ProgressDialog
import android.content.Intent
import android.media.session.MediaSession
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.adapter.ChatAdapter
import com.app.student.networking.fragments.model.Chat
import com.google.android.gms.common.api.Api
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.app.student.networking.fragments.model.Users
import com.app.student.networking.notification.*
import com.google.android.gms.tasks.Continuation
import kotlinx.android.synthetic.main.activity_message_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageChatActivity : AppCompatActivity() {

    var userIdVisit: String = ""
    var firebaseUser: FirebaseUser? = null
    var chatsAdapter: ChatAdapter? = null
    var mChatList: List<Chat>? = null
    lateinit var recycler_view_chats: RecyclerView
    var reference: DatabaseReference? = null

    var notify = false
    var apiservice: APIService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_message_chat)
//        setSupportActionBar(toolbar)
        //supportActionBar!!.title = ""
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this@MessageChatActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        apiservice = Client.Client.getClient("https://fcm.googleapis.com/")!!.create(APIService::class.java)



        intent = intent
        //here
        userIdVisit = intent.getStringExtra("visit_id").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recycler_view_chats = findViewById(R.id.recycler_view_chats)
        recycler_view_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycler_view_chats.layoutManager = linearLayoutManager


        reference = FirebaseDatabase.getInstance().reference
            .child("Users").child(userIdVisit)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                val user: Users? = p0.getValue(Users::class.java)

                username_mchat.text = user!!.getUserName()
                Picasso.get().load(user.getProfile()).into(profile_image_mchat)

                retrieveMessages(firebaseUser!!.uid, userIdVisit, user.getProfile())

            }

            override fun onCancelled(p0: DatabaseError)
            {

            }

        })


        send_message_btn.setOnClickListener{

            notify = true
            val message = text_message.text.toString()
            if (message == "")
            {
                Toast.makeText( this@MessageChatActivity, "Please type a message before sending...", Toast.LENGTH_LONG).show()
            }else
            {
                sendMessageToUser(firebaseUser!!.uid, userIdVisit, message)
            }
            text_message.setText("")
        }


        attach_image_file_btn.setOnClickListener{

            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Pick Image"), 438)
        }

        seenMessage(userIdVisit)
    }



    private fun sendMessageToUser(senderId: String, receiverId: String, message: String)
    {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String, Any>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["isseen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey!!
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    //Sender
                    val chatListReference = FirebaseDatabase.getInstance()
                        .reference
                        .child("ChatList")
                        .child(firebaseUser!!.uid)
                        .child(userIdVisit)

                    chatListReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (!p0.exists())
                            {
                                chatListReference.child("id").setValue(userIdVisit)
                            }

                            //Receiver
                            val chatListReceiverRef = FirebaseDatabase.getInstance()
                                .reference
                                .child("ChatList")
                                .child(userIdVisit)
                                .child(firebaseUser!!.uid)

                            chatListReceiverRef.child("id").setValue(firebaseUser!!.uid)

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })

                }
            }

        //Implement the push notifications using FCM

        val usersReference = FirebaseDatabase.getInstance().reference
            .child("Users").child(firebaseUser!!.uid)
        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                val user = p0.getValue(Users::class.java)
                if (notify)
                {
                    sendNotification(receiverId, user!!.getUserName(), message)

                }
                notify = false
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }

    private fun sendNotification(receiverId: String, userName: String?, message: String)
    {
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val query = ref.orderByKey().equalTo(receiverId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                for (dataSnapshot in p0.children)
                {
                    val token: Token ? = dataSnapshot.getValue(Token::class.java)
                    val data = Data(
                        firebaseUser!!.uid,
                        R.mipmap.ic_launcher,
                        "$userName : $message",
                        "New Message",
                        userIdVisit
                    )
                    val sender = Sender(data!!, token!!.getToken().toString())

                    apiservice!!.sendNotification(sender).enqueue(object : Callback<MyResponse> {
                        override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>)
                        {
                            if (response.code() == 200)
                            {
                                if (response.body()!!.success !== 1)
                                {
                                    Toast.makeText(this@MessageChatActivity, "Faile, Nothing happened", Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {


                        }
                    })
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data!!.data != null)
        {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("The picture is being uploaded. Please wait...")
            progressBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")


            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->

                if (!task.isSuccessful)
                {
                    task.exception?.let {
                        throw it
                    }
                }

                return@Continuation filePath.downloadUrl

            }).addOnCompleteListener { task ->

                if (task.isSuccessful)
                {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String, Any>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "sent you an image."
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["isseen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId!!

                    ref.child("Chats").child(messageId).setValue(messageHashMap)
                        .addOnCompleteListener { task -> if (task.isSuccessful)
                        {
                            progressBar.dismiss()

                            //Implement the push notifications using FCM

                            val reference = FirebaseDatabase.getInstance().reference
                                .child("Users").child(firebaseUser!!.uid)
                            reference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot)
                                {
                                    val user = p0.getValue(Users::class.java)
                                    if (notify)
                                    {
                                        sendNotification(userIdVisit, user!!.getUserName(), "sent you an image.")

                                    }
                                    notify = false
                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }

                            })

                        }

                        }

                }

            }

        }
    }

    private fun retrieveMessages(senderId: String?, receiverId: String?, receiverImageUrl: String?)
    {
        mChatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot)
            {
                (mChatList as ArrayList<Chat>).clear()
                for (snapshot in p0.children)
                {
                    val chat = snapshot.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(senderId) && chat.getSender().equals(receiverId)
                        || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId))
                    {

                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    chatsAdapter = ChatAdapter(this@MessageChatActivity, (mChatList as ArrayList<Chat>), receiverImageUrl!!)
                    recycler_view_chats.adapter = chatsAdapter
                }

            }
            override fun onCancelled(p0: DatabaseError) {

            }


        })

    }


    var seenListener: ValueEventListener? = null

    private fun seenMessage(userId: String)
    {

        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        seenListener = reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (datasnapshot in p0.children)
                {
                    val chat = datasnapshot.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && chat!!.getSender().equals(userId))
                    {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isseen"] = true
                        datasnapshot.ref.updateChildren(hashMap)


                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })

    }

    override fun onPause() {
        super.onPause()

        reference!!.removeEventListener(seenListener!!)

    }
}

