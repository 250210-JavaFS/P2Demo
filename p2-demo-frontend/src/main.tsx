import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { AuthProvider } from './GlobalData/AuthContext.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    {/* Wrapping all of the components defined in App.tsc with our AuthProvider */}
    {/* This gives us the ability to share user data and mutator to any component */}
    <AuthProvider>
      <App />
    </AuthProvider>
  </StrictMode>,
)
