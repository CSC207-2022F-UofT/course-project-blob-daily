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

function App() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [sessionId, setSessionID] = useState("");

    const [usernameFailed, setUsernameFailed] = useState(false)


    const loginReq = `http://localhost:8080/login?username=${username}&password=${password}`

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
                    console.log(`invalid: ${response.status}`);
                    setUsernameFailed(true);
                }
            }).then((data) => {
                return data.id;
            })
        )
        const sesid = await session;
        setSessionID(sesid)
        console.log(sesid)

    }

    function handleUsername(e){
        setUsername(e.target.value)
    }

    function handlePassword(e){
        setPassword(e.target.value)
    }



  return (

          <Routes>
              <Route path="/" element={<Login
                  handleLoginClick={handleLoginClick}
                  username={username}
                  password={password}
                  handleUsername={handleUsername}
                  handlePassword={handlePassword}
                  usernameFailed = {usernameFailed}
              />} />
              <Route path="/home" element={<Home sessionId={sessionId}/>} />
              <Route path="/friends" element={<Friends sessionId={sessionId}/>} />
              <Route path="/shop" element={<Shop sessionId={sessionId}/>} />
              <Route path="/settings" element={<Settings sessionId={sessionId}/>} />
          </Routes>

  );
}

export default App;
