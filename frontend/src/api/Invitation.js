import { Component } from "react";

var session = "";
class Invitation extends Component{
    constructor(props) {
        super(props);
        this.state = {invitations: []};
        // this.remove = this.remove.bind(this);
        // this.handleBuy = this.handleBuy.bind(this);
    }
    
    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/friends/getInvite?sessionID=${session}&isReceiver=true`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({invitations: body});
        }
    }

    async handleAccept(id) {
        const response = await fetch(`http://localhost:8080/acceptInvite?senderID=${id}&receiverID=${session}`)
    }

    async handleDecline(id) {
        const response = await fetch(`http://localhost:8080/declineInvite?senderID=${id}&recieverID=${session}`)
    }

    render() {
        const {invitations} = this.state;

        if (invitations.length > 0){
            return (
                <div>
                    <table>
                        <thead>
                            <td>Username</td>
                            <td>Actions</td>
                        </thead>
                        <tbody>
                            {
                                invitations.map(invitation =>
                                    <tr key= {invitation.id}>
                                        <td>{invitation}</td>
                                        <td>
                                            <button onClick={this.handleAccept(invitation.id)}>Accept</button>
                                            <button onClick={this.handleDecline(invitation.id)}>Decline</button>
                                        </td>

                                    </tr>
                                )
                            }
                        </tbody>
                    </table>


                    {invitations.map(invitation =>
                        <div key={invitation.id}>
                            {invitation}
                        </div>
                    )}
                </div>
            )
        }
    }
}

export default Invitation;