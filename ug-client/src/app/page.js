"use client"

import { exportNextPath } from '@/config/client';
import { useRouter } from 'next/navigation'
import { useEffect } from 'react';

export default function App() {
  const router = useRouter();
  useEffect(() => {
    router.replace(exportNextPath("/home"));
  }, [])
  return null;
}
