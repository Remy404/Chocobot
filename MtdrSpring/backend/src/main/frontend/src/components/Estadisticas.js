import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);


function Estadisticas() {
  const data = {
    labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"],
    datasets: [
      {
        label: "Ventas",
        data: [12, 19, 3, 5, 2, 3], // Cambia estos datos según necesites
        backgroundColor: "rgba(75, 192, 192, 0.6)", // Color de las barras
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Gráfica de Ventas",
      },
    },
  };
  return (
    <div>
      <h2>Gráfica de Ventas</h2>
      <Bar data={data} options={options} />
    </div>
  );
}

export default Estadisticas;
