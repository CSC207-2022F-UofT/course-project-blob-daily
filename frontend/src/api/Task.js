import { Component } from "react";

class Task extends Component{
    state = {
        tasks: []
    };

    async componentDidMount() {
        const response = await fetch('http://localhost:8080/activetasks');
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({tasks: body});
        }
    }

    render() {
        const {tasks} = this.state;

        if (tasks.length > 0) {
            return (
                <div>
                    <h2>Task</h2>
                    {tasks.map(task =>
                        <div key={task.id}>
                            {task.name} {task.reward}
                        </div>
                    )}
                </div>
            )
        }         
    }
}

export default Task;