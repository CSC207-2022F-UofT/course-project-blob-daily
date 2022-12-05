import Navbar from "../Components/navbar";
import "./home.css"
import TaskApi from "../api/TaskApi";
import AccountApi from "../api/AccountApi";
import FeedApi from "../api/FeedApi";

function Home(props){
    return(
        <div>
            <Navbar curPage={0}/>
            <div className="homeMain">
                <div className="feed">
                    <h2><AccountApi sessionId={props.sessionId}/></h2>
                    <FeedApi sessionId={props.sessionId} />
                </div>
                <div className="goal">
                    <div className="taskAPI">
                        <TaskApi sessionId={props.sessionId}/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home;