import "./Shop.css"
import Navbar from "../Components/navbar";
import 'scrollable-component';
import ShopItem from "../Components/shopitems";
import Item from "../api/Item";
import Pet from "../api/Pet"

function Shop(){
    return(
        <div>
            <Navbar curPage={2}/>
            <div className="shopDisplay">
                <div>
                    {/* <Pet/> */}
                    {/* <div className="avatar"></div>
                    <h1 className="shopText">Current Balance:</h1>
                    <h1 className="shopText">$2400</h1> */}
                </div>

                <div>
                    <Item/>
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