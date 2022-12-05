import Navbar from "../Components/navbar";
import React, { useState} from "react";
import "./Friends.css"
import FriendsApi from "../api/FriendsApi";
import AccountApi from "../api/AccountApi";
import IncomingInvitationsApi from "../api/IncomingInvitationsApi";
import OutgoingInvitationsApi from "../api/OutgoingInvitationsApi";
import ShowError from "../Components/showError";

function Friends(props){

    const [addFriend, setAddFriend] = useState("");
    // const [removeFriend, setRemoveFriend] = useState("");

    const addReq = `http://localhost:8080/friends/sendInvite?receiverUsername=${addFriend}&sessionID=${(props.sessionId)}`;

    function handleAddClick() {
        fetch(addReq, {
            method: "post"
        }).then((response) => {
            if (response.status === 201 || response.status === 200) {
                console.log("add success")
            }
            return response.json();
        }).then((data) => {
            console.log(data);
        })
    }

    return(
        <div>
            <Navbar curPage={1}/>
            <div className="FriendsMain">
                <div className="Friends">
                <h3>Friends</h3>
                    <FriendsApi sessionId={props.sessionId}/>
                </div>
                <div>
                    <h3 className="requestTitle">Incoming Requests</h3>
                    <div className="Request">
                        <IncomingInvitationsApi sessionId={props.sessionId}/>
                        {/* <div className="RequestUser">
                            <p>@TonyKim</p>
                        </div> */}
                        {/*<div className="RequestAccept">*/}
                        {/*    <p className="accept">Accept</p>*/}
                        {/*</div>*/}
                        {/*<div>*/}
                        {/*    <p className="blue">/</p>*/}
                        {/*</div>*/}
                        {/*<div>*/}
                        {/*    <p className="decline">Decline</p>*/}
                        {/*</div>*/}
                        {/* <ShowError error={"Friend no longer doesn't work"}/> */}
                    </div>
                </div>
                <div>
                    <h3 className="requestTitle">Outgoing Requests</h3>
                    <div className="Request">
                        <OutgoingInvitationsApi sessionId={props.sessionId}/>
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
                        <button className="blueButton" onClick={handleAddClick}>Send</button>
                    </div>

                    {/* <h3>Remove Friend</h3>
                    <div className="friendForm">
                        <input
                            className="answerBox"
                            value={removeFriend}
                            onChange={(e) => setRemoveFriend(e.target.value)}
                        />
                        <button className="blue" onClick={handleRemoveClick}>Submit</button>
                    </div>
                    <ShowError error={"Friend doesn't exist"}/> */}
                </div>
            </div>
        </div>
    )
}

export default Friends