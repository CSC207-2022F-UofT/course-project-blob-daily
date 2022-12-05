import Navbar from "../Components/navbar";
import "./home.css"
import TaskApi from "../api/TaskApi";
import AccountApi from "../api/AccountApi";
import FeedApi from "../api/FeedApi";

function Home(props){
    return(
        <div>
            <Navbar curPage={0}/>
            <AccountApi sessionId={props.sessionId} className="alignRight"/>
            <div className="homeMain">

                <div className="feed">
                    <h2>Add Friends to see their accomplishments</h2>
                    <FeedApi sessionId={props.sessionId} />
                </div>
                <div className="goal">
                    <h2>Daily Challenge</h2>
                    <p>Do something out of your comfort zone</p>
                    <TaskApi sessionId={props.sessionId}/>                    
                </div>

            </div>

        </div>
    )
}

export default Home;