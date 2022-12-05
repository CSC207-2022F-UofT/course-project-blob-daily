import { Component } from "react";

class FriendsApi extends Component{
    constructor(props) {
        super(props);
        this.state = {friends: []};
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/friends/getFriends?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();

        if (response.status === 200) {
            this.setState({friends: body.friends});
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/friends/getFriends?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({friends: body.friends});
        }
    }

    async handleUnfriend(username) {
        await fetch(`http://localhost:8080/friends/deleteFriend?sessionID=${(this.props.sessionId)}&friendUserName=${username}`, {method: 'DELETE'})
    }

    render() {
        const {friends} = this.state;
        if (friends){
            return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <td></td>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                friends.map(friend =>
                                    <tr key={friend.username}>
                                        <td>{friend.username}</td>
                                        <td>
                                            <button onClick={() =>
                                            this.handleUnfriend(friend.username)}>Unfriend</button>
                                        </td>
                                    </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            )
        }
    }
}

export default FriendsApi;