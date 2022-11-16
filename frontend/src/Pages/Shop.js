import "./Shop.css"
import Navbar from "../Components/navbar";
import 'scrollable-component';
import ShopItem from "../Components/shopitems";

function Shop(){
    return(
        <div>
            <Navbar curPage={2}/>
            <div className="shopDisplay">
                <div>
                    <div className="avatar"></div>
                    <h1 className="shopText">Current Balance:</h1>
                    <h1 className="shopText">$2400</h1>
                </div>

                <div>
                    <ShopItem/>
                    <div className="manageFlex">
                        <p>Equip</p>
                        <p>|</p>
                        <p>Reset</p>
                    </div>

                </div>

            </div>

        </div>


    )
}

export default Shop