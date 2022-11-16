import { Component } from "react";

class Task extends Component{
    state = {
        tasks: []
    };

    async componentDidMount() {
        const response = await fetch('/');
        const body = await response.json();
        this.setState({tasks: body})
    }

    render() {
        const {tasks} = this.state;
        return (
            <div>
                <h2>Tasks</h2>
                
                {console.log(tasks)}
                {/* {tasks.map(task =>
                    <div>
                        {task.task} {task.reward}
                    </div>
                )} */}
            </div>
        )
    }
}



export default Task;