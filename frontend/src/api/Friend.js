import { Component } from "react";

class Friend extends Component{
    constructor(props) {
        super(props);
        this.state = {friends: []};
        // this.remove = this.remove.bind(this);
        // this.handleBuy = this.handleBuy.bind(this);
    }


    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/getFriend?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({friends: body});
        }
    }

    render() {
        const {friends} = this.state;
        if (friends.length > 0){
            return (
                <div>
                    {friends.map(friend =>
                        <div key={friend.id}>
                            {friend}
                        </div>
                    )}
                </div>
            )
        }
    }
}

export default Friend;