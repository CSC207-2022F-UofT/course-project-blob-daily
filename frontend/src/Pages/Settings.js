import './Settings.css'
import Navbar from "../Components/navbar";
import React, {useState} from "react";

function Settings(){
    const [username, setUsername] = useState("new username ...");
    const [password, setPassword] = useState("new password ...");

    return(
        <div>
            <Navbar />
                <div className="settingsForm">
                    <input
                        className="answerBox"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <p className="blue">Send</p>
                </div>

                <div className="settingsForm space">
                    <input
                        className="answerBox"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <p className="blue">Send</p>
                </div>

                <h3 className="account">Delete account</h3>

                <h3 className="account">Logout</h3>

        </div>
    )
}

export default Settings