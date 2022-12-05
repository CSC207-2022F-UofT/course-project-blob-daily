import { Component } from "react";

class ItemsApi extends Component{
    constructor(props) {
        super(props);
        this.state = {items: [], error: false};
    }

    async componentDidMount() {
        const response = await fetch('http://localhost:8080/shopItems');
        const body = await response.json();
        
        if (response.status === 200) {
            body.splice(0, 12);
            this.setState({items: body});
        }
        
    }

    async handleBuy(id) {
        const response = await fetch(`http://localhost:8080/purchaseItems?sessionID=${(this.props.sessionId)}&itemId=${id}`, {method: 'POST'})

        if (response.status === 200) {
            this.setState({error: false})
        } else {
            this.setState({error: true})
        }
    }

    

    render() {
        const {items, error} = this.state;
        if (items){
            return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <td>Item</td>
                                <td></td>
                                <td>Description</td>
                                <td>Cost</td>
                                <td></td>
                            </tr>
                        </thead>

                        <tbody>
                                {
                                items.map(item =>
                                    <tr key ={item.id}> 
                                        <td>{item.name}</td>
                                        <td>
                                            <img
                                                src={item.imageLink}
                                                width={30}
                                            />
                                        </td>
                                        <td>{item.description}</td>
                                        <td>{item.cost}</td>
                                        <td>
                                            <button onClick={() =>
                                                this.handleBuy(item.id)}>Buy</button>
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                    <div>
                        {error ? <p>Could not purchase item</p>: <p></p>}
                    </div>
                </div>
                
            )
         }
    }
}

export default ItemsApi;