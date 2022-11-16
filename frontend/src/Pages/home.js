import Navbar from "../Components/navbar";
import "./home.css"
import Feeditem from "../Components/feeditem";

function Home(){
    // function createTaskCompletion(){
    //     fetch("/home",{
    //         method: "POST",
    //     }).then((response) => {
    //         if (response.status === 200) return response.json();
    //     }),then((data) =>{
    //         console.log(data);
    //     });
    // }


    return(
        <div>
            <Navbar curPage={0}/>
            <div className="homeMain">
                <div className="feed">
                    <Feeditem />
                    <Feeditem />
                    <Feeditem />
                </div>
                <div className="goal">
                    <h2>Daily Challenge</h2>
                    <p>Do something out of your comfort zone</p>
                    {/* <div className="progress">
                        <div className="fillBar"/>
                        
                    </div> */}
                    {/* <button onClick={()=> createTaskCompletion}>
                            Complete Task
                    </button> */}
                </div>
            </div>

        </div>
    )
}

export default Home;