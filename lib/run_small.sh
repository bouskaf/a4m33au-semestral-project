cat ../output/nadr_small_switch.p | ./tptp_to_ladr | ./prover9 > ../output/nadr_small_switch.proof
cat ../output/nadr_small_collision.p | ./tptp_to_ladr | ./prover9 > ../output/nadr_small_collision.proof
cat ../output/nadr_small_block.p | ./tptp_to_ladr | ./prover9 > ../output/nadr_small_block.proof