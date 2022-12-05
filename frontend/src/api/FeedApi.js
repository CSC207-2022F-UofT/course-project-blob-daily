import { Component } from "react";
import { resolvePath } from "react-router-dom";
import './FeedApi.css'

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

                    {/* <div className="feedItem">
                        <div>
                            <div className="feedPhoto">
                                <img 
                                    src="https://wallpaperaccess.com/full/1480472.jpg"
                                    width={600} alt="feed image"
                                />
                            </div>
                            <div>@username11 - Mon Dec 05</div>
                            <div>---</div>
                            <div>Allnighter inside Robarts</div>
                        </div>
                    </div> */}
                    
                    {
                        feeds.map((feed, index) =>
                            <div className="feedItem">
                                <div key ={index}>
                                    <div className="feedPhoto">
                                        <img 
                                            src={feed.record.image}
                                            width={600} alt="feedImage"
                                        />
                                    </div>
                                    
                                    <div>
                                        @{feed.account.username} - {feed.record.date}
                                    </div>
                                    <div>---</div>
                                    <div>
                                        {feed.record.name}
                                    </div> 
                                </div>
                            </div>
                        )
                    } 

                </div>

            )
        }         
    }
}

export default FeedApi;