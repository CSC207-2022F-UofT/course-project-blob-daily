import { Component } from "react";
import { resolvePath } from "react-router-dom";

class FeedApi extends Component{
    constructor(props) {
        super(props);
        this.state = {feeds: []};
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/feed?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({feeds: body});
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/feed?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({feeds: body});
        }
    }

    render() {
        const {feeds} = this.state;
        if (feeds) {
            return (
                <div>
                    <table>  
                        <tbody>
                            {
                                feeds.map((feed, index) =>
                                    <tr key ={index}>
                                        <td>{feed.account.username}</td>
                                        {/* <td>
                                            {
                                                feed.pet.map(outfit =>
                                                    <tr key={outfit.id}>
                                                        <td>{outfit.name}</td>
                                                    </tr>
                                                )
                                            }
                                        </td> */}
                                        <td>{feed.record.name}</td> 
                                        <td>
                                            <img 
                                                src={feed.record.image}
                                                width={600}
                                            />
                                        </td>
                                        <td>{feed.record.date}</td>
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

export default FeedApi;