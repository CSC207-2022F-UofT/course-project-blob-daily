import "./navbar.css"
import {Link} from "react-router-dom";


function navbar(){
    return(
        <div>
            <h1 className="navTitle">Quest Pets</h1>
            <div className="navList">
                <Link to='/' className="navItem">Home</Link>
                <p>/</p>
                <Link to='/friends' className="navItem">Friends</Link>
                <p>/</p>
                <Link to='/shop' className="navItem">Shop</Link>
                <p>/</p>
                <Link to='/settings' className="navItem">Settings</Link>
            </div>
        </div>
    )
}

export default navbar;