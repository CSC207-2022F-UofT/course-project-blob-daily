import "./feeditem.css"

function Feeditem(){
    return(
        <div>
            <div className="feedPicture"></div>
            <div className="caption">
                <span className="author">@Kurtislaw </span>
                <span>Weekly challenge - Seek discomfort</span>
                <p>---</p>
                <p>"Travelled across the world with no phone or money"</p>
            </div>
        </div>
    )
}

export default Feeditem