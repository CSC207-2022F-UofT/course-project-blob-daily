import Navbar from "../Components/navbar";
import "./home.css"
import Feeditem from "../Components/feeditem";

function Home(){
    return(
        <div>
            <Navbar curPage={0}/>
            <div className="homeMain">
                <div className="feed">
                    <h2>Add Friends to see their accomplishments</h2>
                    <Feeditem />
                    <Feeditem />
                    <Feeditem />
                </div>
                <div className="goal">
                    <h2>Daily Challenge</h2>
                    <p>Do something out of your comfort zone</p>
                    <div className="progress">
                        <div className="fillBar"/>
                        <p>0/1</p>
                    </div>

                </div>
            </div>

        </div>
    )
}

export default Home;