import React from "react";
import "./Metas.css";

const metaFinanceira = 10000; // Meta total em reais
const valorAtual = 3400; // Valor já juntado
const progresso = (valorAtual / metaFinanceira) * 100; // Porcentagem atingida

const Metas: React.FC = () => {
  return (
    <div className="metas-container">
      <h2 className="metas-title">Progresso da Meta</h2>

      <div className="metas-bar-container">
        <div className="metas-bar" style={{ width: `${progresso}%` }}></div>
      </div>

      <p className="metas-text">
        R$ {valorAtual.toLocaleString()} / R$ {metaFinanceira.toLocaleString()}
      </p>
    </div>
  );
};

export default Metas;
