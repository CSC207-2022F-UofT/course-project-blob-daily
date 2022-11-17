import './Settings.css'
import Navbar from "../Components/navbar";
import React, {useCallback, useState} from "react";
import {useNavigate} from "react-router-dom";

function Settings(){
    const [username, setUsername] = useState("new username ...");
    const [password, setPassword] = useState("new password ...");

    const navigate = useNavigate();

    const handleLogoutClick = useCallback(() => navigate('/', {replace: true}), [navigate]);

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

                <div className="settingsForm space">
                    <input
                        className="answerBox"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <p className="blue">Send</p>
                </div>

                <h3 className="account">Delete account</h3>

                <h3 className="account" onClick={handleLogoutClick}>Logout</h3>

        </div>
    )
}

export default Settings