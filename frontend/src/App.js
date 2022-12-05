import './App.css';
import Home from "./Pages/home";
import Friends from "./Pages/Friends"
import Shop from "./Pages/Shop";
import Settings from "./Pages/Settings";

import {
    BrowserRouter as Router,
    Routes,
    Route, useNavigate
} from "react-router-dom";
import Login from "./Pages/Login";
import {useState} from "react";
import CreateAccount from "./Pages/CreateAccount";

function App() {

    const [username, setUsername] = useState("frontend");
    const [password, setPassword] = useState("abc123!");

    const [createUsername, setCreateUsername] = useState("");
    const [createPassword, setCreatePassword] = useState("");

    const [sessionId, setSessionID] = useState("");

    const [usernameFailed, setUsernameFailed] = useState(false)


    const loginReq = `http://localhost:8080/login?username=${username}&password=${password}`;
    const registerReq = `http://localhost:8080/register?username=${username}&password=${password}`;

    const navigate = useNavigate();


    async function handleLoginClick() {
        const session = (
            fetch(loginReq, {
                method: "post",
            }).then((response) => {
                if (response.status === 200) {
                    console.log("login success");
                    navigate('/home');
                    return response.json();
                } else {
                    setUsernameFailed(true);
                }
            }).then((data) => {
                return data.sessionID;
            })
        )
        const sesid = await session;
        setSessionID(sesid)
    }

    async function handleRegisterClick(){
        const session = (
            fetch(registerReq, {
                method: "post",
            }).then((response) => {
                if (response.status === 200) {
                    console.log("register success");
                    navigate('/home');
                    return response.json();
                } else {
                    setUsernameFailed(true);
                }
            }).then((data) => {
                return data.sessionID;
            })
        )
        const sesid = await session;
        setSessionID(sesid)
    }


    function handleUsername(e){
        setUsername(e.target.value)
    }

    function handlePassword(e){
        setPassword(e.target.value)
    }

    function handleCreateUsername(e){
        setCreateUsername(e.target.value)
    }

    function handleCreatePassword(e){
        setCreatePassword(e.target.value)
    }


    return (
      <div>
          <Routes>
              <Route path="/" element={<Login
                  handleLoginClick={handleLoginClick}
                  username={username}
                  password={password}
                  handleUsername={handleUsername}
                  handlePassword={handlePassword}
                  usernameFailed = {usernameFailed}
              />} />
              <Route path="/register" element={<CreateAccount
                  handleRegisterClick={handleRegisterClick}
                  username={createUsername}
                  password={createPassword}
                  handleUsername={handleCreateUsername}
                  handlePassword={handleCreatePassword}
              />} />
              <Route path="/home" element={<Home sessionId={sessionId}/>} />
              <Route path="/friends" element={<Friends sessionId={sessionId}/>} />
              <Route path="/shop" element={<Shop sessionId={sessionId}/>} />
              <Route path="/settings" element={<Settings sessionId={sessionId}/>} />
          </Routes>
      </div>

  )
}

export default App;
