import './App.css';
import Home from "./Pages/home";
import Friends from "./Pages/Friends"
import Shop from "./Pages/Shop";
import Settings from "./Pages/Settings";

import {
    BrowserRouter as Router,
    Routes,
    Route
} from "react-router-dom";

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/friends" element={<Friends />} />
              <Route path="/shop" element={<Shop />} />
              <Route path="/settings" element={<Settings />} />
          </Routes>
      </Router>
  );
}

export default App;
