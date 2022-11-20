import { Component } from "react";

class Item extends Component{
    constructor(props) {
        super(props);
        this.state = {items: []};
        // this.remove = this.remove.bind(this);
        // this.handleBuy = this.handleBuy.bind(this);
    }

    show = false;

    async componentDidMount() {
        const response = await fetch('http://localhost:8080/shopItems');
        const body = await response.json();
        
        if (response.status === 200) {
            this.setState({items: body});
            this.show = true;
            
        }
    }

    async handleBuy(id) {
        
    }
    async handleEquip(id) {
        
    }


    render() {
        const {items} = this.state;
        if (items.length > 0){
            return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <td>Item</td>
                                <td>Description</td>
                                <td>Cost</td>
                                <td>Actions</td>
                            </tr>
                        </thead>

                        <tbody>
                            {
                                items.map(item =>
                                    <tr key ={item.id}> 
                                        <td>{item.name}</td>
                                        <td>{item.description}</td>
                                        <td>{item.cost}</td>
                                        <td>
                                            <button onClick={this.handleBuy(item.id)}>Buy</button>
                                            <button onClick={this.handleEquip(item.id)}>Equip</button>
                                        </td>
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

export default Item;