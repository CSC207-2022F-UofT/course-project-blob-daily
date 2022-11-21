import './Settings.css'
import Navbar from "../Components/navbar";
import React, {useCallback, useState} from "react";
import {useNavigate} from "react-router-dom";
import ShowError from "../Components/showError";


function Settings(props){
    const [username, setUsername] = useState("new username ...");
    const [password, setPassword] = useState("new password ...");

    console.log(props.sessionId);

    const navigate = useNavigate();
    const logoutReq = `http://localhost:8080/logout?sessionID=${props.sessionId}`
    const deleteReq = `http://localhost:8080/delete?sessionID=${props.sessionId}`

    function handleLogoutClick(){
        fetch(logoutReq, {
            method: "post"
        }).then((response) => {
            if(response.status === 200) {
                console.log("logout success");
                navigate('/');
            } else {
                console.log(`invalid ${response.status}`)
            }
        })
    }

    function handleDeleteClick(){
        fetch(deleteReq, {
            method: "delete"
        }).then((response) => {
            if (response.status === 200) {
                console.log("delete success");
                navigate('/');
            } else {
                console.log(`invalid ${response.status}`)
            }
        })
    }

    return(
        <div>
            <Navbar curPage={3}/>
                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <p className="blue">Send</p>
                </div>
                <div className="errorTextSettings">
                    <ShowError error={"Invalid Username"}/>
                </div>

                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <p className="blue">Send</p>
                </div>
            <div className="errorTextSettings space">
                <ShowError error={"Invalid Password"}/>
            </div>

                
                <div>
                    <button className="account" onClick={handleDeleteClick}>Delete account</button>
                </div>

                <div>
                    <button className="account" onClick={handleLogoutClick}>Logout</button>
                </div>
                

        </div>
    )
}

export default Settings