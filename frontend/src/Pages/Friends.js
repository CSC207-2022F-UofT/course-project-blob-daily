import Navbar from "../Components/navbar";
import React, { useState} from "react";
import "./Friends.css"

function Friends(){

    const [addFriend, setAddFriend] = useState("Enter Username");
    const [removeFriend, setRemoveFriend] = useState("Enter Username");



    return(
        <div>
            <Navbar curPage={1}/>
            <div className="FriendsMain">
                <div className="Friends">
                    <h3>Friends</h3>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p>
                </div>
                <div>
                    <h3 className="requestTitle">Request</h3>
                    <div className="Request">

                        <div className="RequstUser">
                            <p>@TonyKim</p>
                            <p>@TonyKim</p>
                        </div>
                        <div className="RequestAccept">
                            <p className="accept">Accept</p>
                            <p className="accept">Accept</p>
                        </div>
                        <div>
                            <p className="blue">/</p>
                            <p className="blue">/</p>
                        </div>
                        <div>
                            <p className="decline">Decline</p>
                            <p className="decline">Decline</p>
                        </div>

                    </div>
                </div>
                <div className="friendManagement">
                    <h3>Add Friend</h3>
                    <div className="friendForm">
                        <input
                            className="answerBox"
                            value={addFriend}
                            onChange={(e) => setAddFriend(e.target.value)}
                        />
                        <p className="blue">Send</p>
                    </div>

                    <h3>Remove Friend</h3>
                    <div className="friendForm">
                        <input
                            className="answerBox"
                            value={removeFriend}
                            onChange={(e) => setRemoveFriend(e.target.value)}
                        />
                        <p className="blue">Submit</p>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Friends