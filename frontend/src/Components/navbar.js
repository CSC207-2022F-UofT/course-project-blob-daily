import "./navbar.css"
import {Link} from "react-router-dom";


function navbar(props){
    return(
        <div>
            <h1 className="navTitle">Quest Pets</h1>
            <div className="navList">
                <Link to='/home' className={`navItem ${props.curPage === 0 ? "selected" : ""}`}>Home</Link>
                <p>/</p>
                <Link to='/friends' className={`navItem ${props.curPage === 1 ? "selected" : ""}`}>Friends</Link>
                <p>/</p>
                <Link to='/shop' className={`navItem ${props.curPage === 2 ? "selected" : ""}`}>Shop</Link>
                <p>/</p>
                <Link to='/settings' className={`navItem ${props.curPage === 3 ? "selected" : ""}`}>Settings</Link>
            </div>
        </div>
    )
}

export default navbar;