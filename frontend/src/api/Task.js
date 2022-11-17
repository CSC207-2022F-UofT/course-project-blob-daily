import { Component } from "react";

class Task extends Component{
    state = {
        tasks: []
    };

    async componentDidMount() {
        const response = await fetch('http://localhost:8080/');
        const body = await response.json();
        this.setState({tasks: body});
    }

    render() {
        const {tasks} = this.state;
        return (
            <div>
                <h2>Task</h2>
                {tasks.map(task =>
                    <div key={task.name}>
                        {task.name} {task.reward}
                    </div>
                )}
            </div>
        )
    }
}

export default Task;