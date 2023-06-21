package com.tuk.lightninggathering

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.fragment.app.Fragment

class MemberFragment : Fragment() {

    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member, container, false)
        linearLayout = view.findViewById(R.id.memberLinearLayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memberKeys = arguments?.getStringArrayList("memberKeys")

        if (memberKeys != null) {
            val db = FirebaseDatabase.getInstance()
            val usersRef = db.getReference("users")

            for (memberKey in memberKeys) {
                val query = usersRef.orderByKey().equalTo(memberKey)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (userSnapshot in dataSnapshot.children) {
                            val memberName = userSnapshot.child("name").getValue(String::class.java)
                            val memberEmail = userSnapshot.child("email").getValue(String::class.java)
                            if (memberName != null && memberEmail != null) {
                                activity?.runOnUiThread {
                                    val cardView = CardView(activity!!).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            setMargins(16, 16, 16, 16)  // Increase the margins to give more space
                                        }

                                        radius = 16f
                                        cardElevation = 8f
                                        setCardBackgroundColor(Color.parseColor("#FFFDD0"))  // Light yellow color

                                        val layout = LinearLayout(activity).apply {
                                            orientation = LinearLayout.VERTICAL
                                            layoutParams = LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                            ).apply {
                                                setMargins(8, 8, 8, 8)  // Add margins inside the card for more space
                                            }

                                            val nameTextView = TextView(activity).apply {
                                                text = SpannableString("이름: $memberName").apply {
                                                    setSpan(StyleSpan(Typeface.BOLD), 0, 3, 0)  // Make "Name: " bold
                                                }
                                                textSize = 16f
                                                setTextColor(Color.parseColor("#444444"))
                                                setPadding(16, 16, 16, 16)
                                            }

                                            val emailTextView = TextView(activity).apply {
                                                text = SpannableString("이메일: $memberEmail").apply {
                                                    setSpan(StyleSpan(Typeface.BOLD), 0, 4, 0)  // Make "Email: " bold
                                                }
                                                textSize = 16f
                                                setTextColor(Color.parseColor("#444444"))
                                                setPadding(16, 16, 16, 16)
                                            }

                                            addView(nameTextView)
                                            addView(emailTextView)
                                        }

                                        addView(layout)
                                    }

                                    linearLayout.addView(cardView)
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                    }
                })
            }
        }
    }
}
