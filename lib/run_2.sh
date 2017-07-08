cat ../output/nadr2_switch.p | ./tptp_to_ladr | ./prover9 > ../output/nadr2_switch.proof
cat ../output/nadr2_collision.p | ./tptp_to_ladr | ./prover9 > ../output/nadr2_collision.proof
cat ../output/nadr2_block.p | ./tptp_to_ladr | ./prover9 > ../output/nadr2_block.proof