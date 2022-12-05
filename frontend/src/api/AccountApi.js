import { Component } from "react";

class AccountApi extends Component{
    constructor(props) {
        super(props);
        this.state = {account: []};
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/account?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({account: body});
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/account?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({account: body});
        }
    }

    render() {
        const {account} = this.state;
        return(
            <div className="usernameStyle">
                You are logged in as : {account.username}
            </div>
        )
    }
}

export default AccountApi;