import Navbar from "../Components/navbar";
import React, { useState} from "react";
import "./Friends.css"
import Friend from "../api/Friend"
import Invitation from "../api/Invitation";
import ShowError from "../Components/showError";

function Friends(props){

    const [addFriend, setAddFriend] = useState("");
    const [removeFriend, setRemoveFriend] = useState("");

    const addReq = `http://localhost:8080/sendInvite?sessionID=${props.sessionId}&friendUsername=${addFriend}`;

    function handleAddClick() {
        fetch(addReq, {
            method: "post"
        }).then((response) => {
            if (response.status === 200) {
                console.log("add success")
            } else {
                console.log(`invalid ${response.status}`)
            }
            return response.json();
        }).then((data) => {
            console.log(data);
        })
    }

    function handleRemoveClick() {

    }

    return(
        <div>
            <Navbar curPage={1}/>
            <div className="FriendsMain">
                <div className="Friends">
                    <Friend sessionId={props.sessionId}/>
                    {/* <h3>Friends</h3>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p>
                    <p>@KurtisLaw</p> */}
                </div>
                <div>
                    <h3 className="requestTitle">Request</h3>
                    <div className="Request">
                        <Invitation />
                        <div className="RequestUser">
                            <p>@TonyKim</p>
                        </div>
                        {/*<div className="RequestAccept">*/}
                        {/*    <p className="accept">Accept</p>*/}
                        {/*</div>*/}
                        {/*<div>*/}
                        {/*    <p className="blue">/</p>*/}
                        {/*</div>*/}
                        {/*<div>*/}
                        {/*    <p className="decline">Decline</p>*/}
                        {/*</div>*/}
                        <ShowError error={"Friend no longer doesn't work"}/>

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
                        <button className="blue" onClick={handleAddClick}>Send</button>
                    </div>

                    <h3>Remove Friend</h3>
                    <div className="friendForm">
                        <input
                            className="answerBox"
                            value={removeFriend}
                            onChange={(e) => setRemoveFriend(e.target.value)}
                        />
                        <button className="blue" onClick={handleRemoveClick}>Submit</button>
                    </div>
                    <ShowError error={"Friend doesn't exist"}/>
                </div>
            </div>
        </div>
    )
}

export default Friends