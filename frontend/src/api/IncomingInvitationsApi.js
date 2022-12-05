import { Component } from "react";
import "./PetApi.css"

class IncomingInvitationsApi extends Component{
    constructor(props) {
        super(props);
        this.state = {invitations: []};
        // this.remove = this.remove.bind(this);
        // this.handleBuy = this.handleBuy.bind(this);
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/friends/getInviteAsReceiver?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({invitations: body.invites});
        } 
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/friends/getInviteAsReceiver?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({invitations: body.invites});
        } 
    }

    async handleAccept(username) {
        await fetch(`http://localhost:8080/friends/acceptInvite?receiverUsername=${username}&sessionID=${(this.props.sessionId)}`, {method: 'POST'})
    }

    async handleDecline(username) {
        await fetch(`http://localhost:8080/friends/declineInvite?receiverUsername=${username}&sessionID=${(this.props.sessionId)}`, {method: 'delete'})
    }

    render() {
        const {invitations} = this.state;

        if (invitations){
            return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <td></td>
                                <td></td>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                invitations.map(invitation =>
                                    <tr key= {invitation.username}>
                                        <td>{invitation.username}</td>
                                        <td>
                                            <button className="shopButton" onClick={() =>
                                                this.handleAccept(invitation.username)}>Accept</button>
                                            <button className="shopButton" onClick={() =>
                                                this.handleDecline(invitation.username)}>Decline</button>
                                        </td>

                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            )
        }
    }
}

export default IncomingInvitationsApi;