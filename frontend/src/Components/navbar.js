import "./navbar.css"


function navbar(){
    return(
        <div>
            <h1 className="navTitle">Quest Pets</h1>
            <div className="navList">
                <p className="selected">Home</p>
                <p>/</p>
                <p>Friends</p>
                <p>/</p>
                <p>Shop</p>
                <p>/</p>
                <p>Settings</p>
            </div>
        </div>
    )
}

export default navbar;