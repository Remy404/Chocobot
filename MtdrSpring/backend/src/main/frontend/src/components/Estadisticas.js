import { Pie } from "react-chartjs-2"; // Cambiamos el componente Bar por Pie
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  ArcElement, // Registrar ArcElement para gráficos de pastel
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, Title, Tooltip, Legend, ArcElement);

function Estadisticas() {
  const data = {
    labels: ["Completadas", "Pendientes"],
    datasets: [
      {
        label: "Tareas",
        data: [5,5], // Estos datos se adaptan a una gráfica de pastel
        backgroundColor: [
          "rgba(255, 99, 132, 0.6)", // Color para cada sección de la gráfica
          "rgba(255, 159, 64, 0.6)"
        ],
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false, // Permitir que el gráfico se ajuste al tamaño del contenedor
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Tareas pendientes - Tareas completadas",
      },
    },
  };

  return (
    <div style={{ width: '100%', maxWidth: '500px', height: '500px', overflow: 'hidden' }}>
      <h2>Gráfica de Tareas pendientes - Tareas competadas</h2>
      <Pie data={data} options={options} /> {/* Cambiamos de Bar a Pie */}
    </div>
  );
}

export default Estadisticas;
