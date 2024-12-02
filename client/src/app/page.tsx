import { Navbar } from "@/components/home/navbar";
import { Hero } from "@/components/home/hero-section";
import { Features } from "@/components/home/feature-section";
import { Register } from "@/components/home/register-section";
import { Testimonial } from "@/components/home/testimonial-section";
import { FAQ } from "@/components/home/faq-section";
import { Footer } from "@/components/home/footer";

const HomePage = () => {
  return (
    <div className="flex flex-col justify-center gap-4">
      <Navbar />
      <Hero />
      <Features />
      <Register />
      <Testimonial />
      <FAQ />
      <Footer />
    </div>
  );
};

export default HomePage;
