shared_examples 'a newly created model instance' do
  it 'creates timestamp values' do
    expect(subject.created_at).to be_within(1.second).of(Time.now)
    expect(subject.updated_at).to be_within(1.second).of(Time.now)
  end
end
