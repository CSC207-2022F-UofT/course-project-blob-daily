import "./Shop.css"
import Navbar from "../Components/navbar";
import 'scrollable-component';
import ShopItem from "../Components/shopitems";
import ItemsApi from "../api/ItemsApi";
import PetApi from "../api/PetApi"
import ShowError from "../Components/showError";
import AccountApi from "../api/AccountApi";

function Shop(props){

    


    return(
        <div>
            <Navbar curPage={2}/>
            
            <div className="shopDisplay">
                
                <div>

                    <PetApi sessionId={props.sessionId}/>
                    {/* <div className="avatar"></div>
                    <h1 className="shopText">Current Balance:</h1>
                    <h1 className="shopText">$2400</h1> */}
                </div>

                <div>
                    <ItemsApi sessionId={props.sessionId}/>
                    {/* <div className="manageFlex">
                        <p>Equip</p>
                        <p>|</p>
                        <p>Reset</p>
                    </div> */}
                    {/* <ShowError error={"Not enough money"}/> */}

                </div>

            </div>

        </div>


    )
}

export default Shop