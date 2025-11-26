import React from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";

const data = [
  { name: "dia 31", receita: 1505, despesa: 1200 },
  { name: "dia 7", receita: 1900, despesa: 1200 },
  { name: "dia 10", receita: 0, despesa: 400 },
  { name: "dia 15", receita: 2200, despesa: 1500 },
  { name: "dia 20", receita: 0, despesa: 270 },
];

const EntradaSaida: React.FC = () => {
  return (
    <div className="bg-white p-4 rounded-lg shadow-md">
      <h2 className="text-lg font-semibold mb-4">Visão Geral</h2>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="receita" stroke="#22c55e" strokeWidth={2} name="Receitas" />
          <Line type="monotone" dataKey="despesa" stroke="#ef4444" strokeWidth={2} name="Despesas" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default EntradaSaida;
