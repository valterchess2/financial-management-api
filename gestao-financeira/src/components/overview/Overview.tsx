import "./Overview.css";
import EntradaSaida from "./chart/EntradaSaida";
import Metas from "./chart/Metas";

function Overview() {
  return (
    <div id="overview">
      <EntradaSaida/>
      <Metas/>
    </div>
  );
}

export default Overview;
