import { Component } from "react";

class PetApi extends Component{
    constructor(props) {
        super(props);
        this.state = {pet: [], inventory: [], outfit: []};
    }

    
    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/pet?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({pet: body});
            this.setState({inventory: body.inventory});
            this.setState({outfit: body.currentOutfit})
        }
    }
    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/pet?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({pet: body});
            this.setState({inventory: body.inventory});
            this.setState({outfit: body.currentOutfit})
        }
    }

    async handleEquip(id){
        const response = await fetch(`http://localhost:8080/updatePetOutfit?sessionID=${(this.props.sessionId)}&shopItem=${id}`, {method: 'POST'})
        const body = await response.json();

        if (response.status === 200) {
            console.log("successful equip");
        } 
    }

    

    render() {
        const {pet, inventory, outfit} = this.state; 
        if (pet){
            return (
                <div>
                    <div>
                        <h2>Health</h2>
                        {pet.health}
                        <h2>Balance</h2>
                        {pet.balance}
                    </div>
                    <div>
                        <h2>Inventory</h2>
                        <table>
                            <thead>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </thead>
                            
                            <tbody>
                                {
                                    inventory.map(item =>
                                        <tr key ={item.id}>
                                            <td>{item.name}</td>
                                            <td>
                                                <button onClick={()=>
                                                this.handleEquip(item.id)}>Equip</button>
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                            
                        </table>
                    </div>
                    <div>
                        <h2>Outfit</h2>
                        <table>
                            <thead>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </thead>

                            <tbody>
                                {
                                    outfit.map((cItem, index) =>
                                        <tr key ={index}>
                                            <td>{cItem.name}</td>
                                            <td>
                                                <img
                                                    src= {cItem.imageLink}
                                                    width={60}
                                                />
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>

                        </table>

                        <div className="petDisplay">
                    {console.log(typeof(outfit[2]))}
                    <div className="avatarLayer">
                        <img src="https://cdn.discordapp.com/attachments/1034200471088414760/1048447502069805076/avatar.png" width={500} alt="avatar-layer"/>
                    </div>
                    <div className="hatLayer">
                        <img src="https://cdn.discordapp.com/attachments/1034200471088414760/1048447502350827593/hat-1.png" width={500} alt="hat-layer"/>
                    </div>
                    <div className="shirtLayer">
                        <img src="https://cdn.discordapp.com/attachments/1034200471088414760/1048447504347316254/shirt-2.png" width={500} alt="shirt-layer"/>
                    </div>
                    <div className="pantsLayer">
                        <img src="https://cdn.discordapp.com/attachments/1034200471088414760/1048447503277768747/pants-1.png" width={500} alt="pants-layer"/>
                    </div>
                </div>

                    </div>
                </div>
            )
        }
    }
}

export default PetApi;