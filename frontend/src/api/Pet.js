import { Component } from "react";

class Pet extends Component{
    state = {
        pet: []
    };

    show = false;
   
    async componentDidMount() {
        const session = ""
        const response = await fetch(`http://localhost:8080/pet?sessionID=${session}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({pet: body});
            this.show = true;
        }
    }

    render() {
        const {pet} = this.state;
        if (this.show) {
            return (
                <div>
                    <div>
                        {pet.health} {pet.balance} {pet.inventory} {pet.currentOutfit}
                    </div>
                </div>
            )
        }    
    }
}

export default Pet;