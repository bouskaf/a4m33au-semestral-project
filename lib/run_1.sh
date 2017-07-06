cat ../output/nadr1_switch.p | ./tptp_to_ladr | ./prover9 > ../output/nadr1_switch.proof
cat ../output/nadr1_collision.p | ./tptp_to_ladr | ./prover9 > ../output/nadr1_collision.proof
#cat ../output/nadr1_block.p | ./tptp_to_ladr | ./prover9 > ../output/nadr1_block.proof