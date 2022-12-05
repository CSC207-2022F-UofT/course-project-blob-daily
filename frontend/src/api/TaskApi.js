import { Component } from "react";
import { resolvePath } from "react-router-dom";
class TaskApi extends Component{
    constructor(props) {
        super(props);
        this.state = {tasks: [], image: "", error: false};
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/activeTasks?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({tasks: body});
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/activeTasks?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({tasks: body});
        }
    }

    async handleComplete(name, image, reward) {
        const response = await fetch(`http://localhost:8080/completeTask?sessionID=${(this.props.sessionId)}&task=${name}&image=${image}&reward=${reward}`, {method: 'POST'})
        const body = await response.json();
       

        if (response.status === 200) {
            console.log("successful completion");
            this.setState({image: ""})
            this.setState({error: false})
        } else {
            this.setState({error: true})
        }
    }

    // toBase64() {
    //     const file = document.querySelector('input[type=file]').files[0];
    //     const reader = new FileReader();
    //     reader.readAsDataURL(file);
        
    //     reader.onload = () => {
    //        this.setState({image: reader.result});
    //        const buffer = new Buffer.from(reader.result.split(',')[1], 'base64')
    //        console.log(buffer)
    //     }
    // }

    render() {
        const {tasks, image, error} = this.state;

        if (tasks) {
            return (
                <div>
                    <h2>Daily Challenges</h2>
                    <table>
                        
                        <tbody>
                            {
                                tasks.map(task =>
                                    <tr key ={task.name}>
                                        <td>{task.name}</td>
                                        <td>{task.reward}</td>
                                       
                                        <td>
                                            <button onClick={() =>
                                                this.handleComplete(task.name, 
                                                image, task.reward)}>Complete</button>
                                        </td>
                                        
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                    <div>
                        <input placeholder="Insert image link..." type="text" value={this.state.image} onChange={(e) => this.setState({image: e.target.value})}/>
                    </div>
                    <div>
                        {error ? <p>Could not complete the task</p>: <p></p>}
                    </div>
                </div>
            )
        }         
    }
}

export default TaskApi;