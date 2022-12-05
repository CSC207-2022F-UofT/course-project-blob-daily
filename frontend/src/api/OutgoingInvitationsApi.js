import { Component } from "react";
import "./PetApi.css"

class OutgoingInvitationsApi extends Component{
    constructor(props) {
        super(props);
        this.state = {invitations: []};
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/friends/getInviteAsSender?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({invitations: body.invites});
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/friends/getInviteAsSender?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({invitations: body.invites});
        }
    }

    async handleWithdraw(username) {
        await fetch(`http://localhost:8080/friends/withdrawInvite?receiverUsername=${username}&sessionID=${(this.props.sessionId)}`, {method: 'delete'})
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
                                                this.handleWithdraw(invitation.username)}>Withdraw</button>
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

export default OutgoingInvitationsApi;