import Navbar from "../Components/navbar";
import React, { useState} from "react";
import "./Friends.css"
import ShowError from "../Components/showError";

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

                        <div className="RequestUser">

                            <p>@TonyKim</p>
                            <p className="accept">Accept</p>
                            <p className="blue">/</p>
                            <p className="decline">Decline</p>

                        </div>
                        <ShowError error={"Friend no longer doesn't exist"}/>


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
                    <ShowError error={"Friend doesn't exist"}/>
                    <h3>Remove Friend</h3>
                    <div className="friendForm">
                        <input
                            className="answerBox"
                            value={removeFriend}
                            onChange={(e) => setRemoveFriend(e.target.value)}
                        />
                        <p className="blue">Submit</p>
                    </div>
                    <ShowError error={"Friend doesn't exist"}/>
                </div>
            </div>
        </div>
    )
}

export default Friends