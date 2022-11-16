import './Settings.css'
import Navbar from "../Components/navbar";
import React, {useState} from "react";

function Settings(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function sendLoginRequest(){}
    function deleteAccountRequest(){}
    function sendLogoutRequest(){}

    return(
        <div>
            <Navbar curPage={3}/>
                <h3>Login</h3>
                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="settingsForm space">
                    <input
                        className="answerBox"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <button id="submit" type="button" onClick={()=>sendLoginRequest()}>
                        Login
                    </button>
                </div>  

                <div>
                    <button id="submit" type="button" onClick={()=>deleteAccountRequest()}>
                        Delete Account
                    </button>
                </div>

                <div>
                    <button id="submit" type="button" onClick={()=>sendLogoutRequest()}>
                        Logout
                    </button> 
                </div>

        </div>
    )
}

export default Settings