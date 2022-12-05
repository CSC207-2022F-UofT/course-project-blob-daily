import {Component} from "react";
import "./PetApi.css";

class PetApi extends Component {
    constructor(props) {
        super(props);
        this.state = {pet: [], inventory: [], outfit: [], hatImage: "", bodyImage: "", pantsImage: ""};
    }


    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/pet?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();

        if (response.status === 200) {
            this.setState({pet: body});
            this.setState({inventory: body.inventory});
            this.setState({outfit: body.currentOutfit})


            this.setState({hatImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
            this.setState({bodyImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
            this.setState({pantsImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
        }
    }

    async componentDidUpdate() {
        const response = await fetch(`http://localhost:8080/pet?sessionID=${(this.props.sessionId)}`);
        const body = await response.json();

        if (response.status === 200) {
            this.setState({pet: body});
            this.setState({inventory: body.inventory});
            this.setState({outfit: body.currentOutfit})

            for (let i = 0; i < body.currentOutfit.length; i++) {
                console.log('this ran')
                if (body.currentOutfit[i].location === 0) {
                    this.setState({hatImage: body.currentOutfit[i].imageLink})
                } else if (body.currentOutfit[i].location === 1) {
                    this.setState({bodyImage: body.currentOutfit[i].imageLink})
                } else if (body.currentOutfit[i].location === 2) {
                    this.setState({pantsImage: body.currentOutfit[i].imageLink})
                } else {
                    this.setState({hatImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
                    this.setState({bodyImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
                    this.setState({pantsImage: "https://i.postimg.cc/LsxXyhs3/butt-ass-naked.png"})
                }
            }
        }
    }

    async handleEquip(id) {
        const response = await fetch(`http://localhost:8080/updatePetOutfit?sessionID=${(this.props.sessionId)}&itemID=${id}`, {method: 'POST'})
        const body = await response.json();

        if (response.status === 200) {
            console.log("successful equip");
        }
    }


    render() {
        const {pet, inventory, outfit, hatImage, bodyImage, pantsImage} = this.state;
        if (pet) {
            return (
                <div className="shopFlex">
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
                                    <tr key={item.id}>
                                        <td>{item.name}</td>
                                        <td>
                                            <button className="shopButton" onClick={() =>
                                                this.handleEquip(item.id)}>Equip
                                            </button>
                                        </td>
                                    </tr>
                                )
                            }
                            </tbody>

                        </table>
                    </div>
                    <div className="imageRendering">
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
                                    <tr key={index}>
                                        <td>{cItem.name}</td>
                                        <td>
                                            <img
                                                src={cItem.imageLink}
                                                width={60}
                                            />
                                        </td>
                                    </tr>
                                )
                            }
                            </tbody>

                        </table>
                    </div>
                    </div>
                        <div>
                        <div className="petDisplay">

                            <div className="avatarLayer">
                                <img
                                    src="https://cdn.discordapp.com/attachments/1034200471088414760/1048447502069805076/avatar.png"
                                    width={500} alt="avatar-layer"/>
                            </div>
                            <div className="hatLayer">
                                <img src={hatImage} width={500} alt="hat-layer"/>
                            </div>
                            <div className="shirtLayer">
                                <img src={bodyImage} width={500} alt="shirt-layer"/>
                            </div>
                            <div className="pantsLayer">
                                <img src={pantsImage} width={500} alt="pants-layer"/>
                            </div>
                        </div>

                        </div>
                </div>
            )
        }
    }
}

export default PetApi;