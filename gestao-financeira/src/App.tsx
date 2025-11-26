import './App.css'
import Sidebar from './components/sidebar/Sidebar.tsx';
import Header from './components/header/Header.tsx';
import Overview from './components/overview/Overview.tsx';
import Saldo from './components/saldo/Saldo.tsx';

function App() {

  return (
    
      <div className='container'>
        <Sidebar/>
        <div className='content'>
        <Header/>
        <Saldo/>
        <Overview/>
       </div>
      </div>
  )
}

export default App
