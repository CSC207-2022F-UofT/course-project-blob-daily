import "./showError.css"

function showError(props){
    return (
        <p className="errorText">{props.error}</p>

    )
}

export default showError